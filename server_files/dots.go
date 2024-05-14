package main

import (
	"image"
	"image/color"
	"log"

	"golang.org/x/exp/shiny/driver"
	"golang.org/x/exp/shiny/screen"
	"golang.org/x/mobile/event/key"
	"golang.org/x/mobile/event/lifecycle"
	"golang.org/x/mobile/event/mouse"
	"golang.org/x/mobile/event/paint"
)

var darkGray = color.RGBA{0x30, 0x30, 0x30, 0xff}

type Point struct {
	X, Y int
}

func main() {
	var points []Point

	driver.Main(func(s screen.Screen) {
		w, err := s.NewWindow(&screen.NewWindowOptions{
			Title:  "dots",
			Width:  400,
			Height: 300,
		})
		if err != nil {
			log.Fatal(err)
		}

		defer w.Release()

		for {
			e := w.NextEvent()

			switch e := e.(type) {
			case lifecycle.Event:
				if e.To == lifecycle.StageDead {
					return
				}

			case key.Event:
				if e.Code == key.CodeEscape {
					return
				}

			case mouse.Event: //クリック処理
				if e.Button == mouse.ButtonLeft && e.Direction == mouse.DirPress {
					p := Point{int(e.X), int(e.Y)}
					if len(points) == 10 {
						return
					}
					points = append(points, p)
					w.Send(paint.Event{})
				}

			case paint.Event: //描画処理
				for _, pos := range points {
					r := image.Rect(pos.X-10, pos.Y-10, pos.X+10, pos.Y+10)
					w.Fill(r, darkGray, screen.Src)
				}
				w.Publish()

			case error:
				log.Print(e)
			}
		}
	})

}
