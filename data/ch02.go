package main

import (
	"fmt"
	"regexp"
	"strconv"
	"strings"
)

func main() {

	//標準入力を受け付け
	var in string
	fmt.Scan(&in)
	fmt.Println(strings.Title(convert(in)))
}

func convert(in string) string {

	pattern, err := regexp.Compile("(\\d+)\\.(\\d+)")
	if err != nil {
		fmt.Println(err)
	}

	var st string

	if pattern.MatchString(in) {
		//小数点あり
		Upper := pattern.FindStringSubmatch(in)[1]
		Lower := pattern.FindStringSubmatch(in)[2]
		st = num_to_word(Upper, false) + " point " + num_to_word(Lower, true)
	} else {
		//小数点なし
		st = num_to_word(in, false)
	}

	return st
}

func num_to_word(num string, b bool) string {
	var ret string
	var dict = map[int]string{}
	dict[0] = "zero"
	dict[1] = "one"
	dict[2] = "two"
	dict[3] = "three"
	dict[4] = "four"
	dict[5] = "five"
	dict[6] = "six"
	dict[7] = "seven"
	dict[8] = "eight"
	dict[9] = "nine"
	dict[10] = "ten"
	dict[11] = "eleven"
	dict[12] = "twelve"
	dict[13] = "thirteen"
	dict[14] = "fourteen"
	dict[15] = "fifteen"
	dict[16] = "sixteen"
	dict[17] = "seventeen"
	dict[18] = "eighteen"
	dict[19] = "nineteen"
	dict[20] = "twenty"
	dict[30] = "thirty"
	dict[40] = "forty"
	dict[50] = "fifty"
	dict[60] = "sixty"
	dict[70] = "seventy"
	dict[80] = "eighty"
	dict[90] = "ninety"
	if b {
		for _, c := range strings.Split(num, "") {
			n, _ := strconv.Atoi(c)
			ret += dict[n] + " "
		}

	} else {
		if len(num) > 12 {
			fmt.Println("桁が多すぎます")
		}
		sp_num := strings.Split(num, "")
		//分割した後順番を逆順にする。
		for i := 0; i < len(sp_num)/2; i++ {
			sp_num[i], sp_num[len(sp_num)-1-i] = sp_num[len(sp_num)-1-i], sp_num[i]
		}
		var f, s, t int
		var norm, thousand, million string
		var stop int = len(num) / 3

		for n, c := range sp_num {
			//三桁ごと更新
			rank := n + 1 //桁数
			mod := rank % 3
			n, _ = strconv.Atoi(c)
			if mod == 1 {
				f = n
			} else if mod == 2 {
				s = n
			} else if mod == 0 {
				t = n
			}

			if rank != len(num) {

				if rank == 3 { //0~999
					norm = triple(f, s, t)

				} else if rank == 6 { //1000~999999
					thousand = triple(f, s, t)

				} else if rank == 9 {
					million = triple(f, s, t)

				}
			} else if rank == len(num) {
				var temp string
				if mod == 1 {
					if f != 0 {
						temp += dict[f]
					} else {
						ret = "-1"
					}
				} else if mod == 2 {
					if s != 1 && s != 0 {
						temp += dict[s*10] + " "
						if f != 0 {
							temp += dict[f]
						}
					} else if s == 1 {
						temp += dict[10*s+f]
					} else if f != 0 {
						temp += dict[f]
					}
				} else if mod == 0 {
					temp = triple(f, s, t)
				}
				ret = temp
				stop = (len(num) - 1) / 3
				if stop == 1 {
					ret += " thousand "
					ret += norm
				} else if stop == 2 {
					ret += " million "
					ret += thousand + " thousand " + norm
				} else if stop == 3 {
					ret += " billion "
					ret += million + " million " + thousand + " thousand " + norm
				}
			}

		}

	}
	return ret
}

func triple(f int, s int, t int) string {
	var dict = map[int]string{}
	dict[0] = "zero"
	dict[1] = "one"
	dict[2] = "two"
	dict[3] = "three"
	dict[4] = "four"
	dict[5] = "five"
	dict[6] = "six"
	dict[7] = "seven"
	dict[8] = "eight"
	dict[9] = "nine"
	dict[10] = "ten"
	dict[11] = "eleven"
	dict[12] = "twelve"
	dict[13] = "thirteen"
	dict[14] = "fourteen"
	dict[15] = "fifteen"
	dict[16] = "sixteen"
	dict[17] = "seventeen"
	dict[18] = "eighteen"
	dict[19] = "nineteen"
	dict[20] = "twenty"
	dict[30] = "thirty"
	dict[40] = "forty"
	dict[50] = "fifty"
	dict[60] = "sixty"
	dict[70] = "seventy"
	dict[80] = "eighty"
	dict[90] = "ninety"

	var rt string

	if t != 0 {
		rt = dict[t] + " hundred "
	}
	if s != 1 && s != 0 {
		rt += dict[s*10] + " "
		if f != 0 {
			rt += dict[f]
		}
	} else if s == 1 {
		rt += dict[10*s+f]
	} else if f != 0 {
		rt += dict[f]
	}
	return rt
}
