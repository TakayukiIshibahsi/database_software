package main

import "fmt"

func 名前表示(名前 string) {
	fmt.Printf("こんにちは%sさん", 名前)
}

func main() {
	var name string
	fmt.Println("名前を入力してください")
	fmt.Scan(&name)
	名前表示(name)
}
