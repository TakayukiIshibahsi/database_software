package main

import (
	"fmt"
	"sync"
	"time"
)

var wg sync.WaitGroup
var a int

func jobplus() {
	defer wg.Done()
	for i := 0; i < 10; i++ {
		t := a
		a = a + 1
		time.Sleep(10 * time.Millisecond)
		fmt.Printf("plus %d->%d\n", t, a)
	}
}

func jobminus() {
	defer wg.Done()
	for i := 0; i < 10; i++ {
		t := a
		a = a - 1
		time.Sleep(11 * time.Millisecond)
		fmt.Printf("minus %d->%d\n", t, a)
	}
}

func main() {
	a = 10

	wg.Add(2)

	go jobplus()
	go jobminus()

	wg.Wait()

}
