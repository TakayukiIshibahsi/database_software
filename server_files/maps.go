package main

import "fmt"

func main() {
	var m1 = map[string]int{"Pochi": 5, "Kenta": 3, "Sally": 5, "Tommy": 7}
	fmt.Print(m1, "\n")
	m1["Kenta"] = 4
	delete(m1, "Tommy")
	fmt.Print(m1, "\n")

	fmt.Printf("m1[Pochi]=%d\n", m1["Pochi"])

	var m2 = make(map[string]int)

	m2["Tommy"] = 18
	m2["Tina"] = 14
	m2["Asa"] = 25

	fmt.Print(m2, "\n")
}
