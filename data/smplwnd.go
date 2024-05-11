package main

import (
	"log"

	"golang.org/x/exp/shiny/driver"
	"golang.org/x/exp/shiny/screen"
	"golang.org/x/mobile/event/key"
	"golang.org/x/mobile/event/lifecycle"
)

func main() {
	driver.Main(func(s screen.Screen) {
		w, err := s.NewWindow(&screen.NewWindowOptions{
			Title:  "simple window",
			Height: 300,
			Width:  400,
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
			case error:
				log.Print(e)
			}
		}
	})
}
