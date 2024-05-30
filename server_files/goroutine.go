package main

import (
	"fmt"
	"time"
)

func print_job(s string) {
	for i := 0; i < 10; i++ {
		time.Sleep(1 * time.Millisecond)
		fmt.Print(s)
	}
}

func main() {
	fmt.Println("A start")
	go print_job("A")

	fmt.Println("B start")
	go print_job("B")

	fmt.Println("C start")
	go print_job("C")

	time.Sleep(1 * time.Second)
	fmt.Println("\nend")
}
