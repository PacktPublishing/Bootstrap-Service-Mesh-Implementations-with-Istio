package main

import (
	_ "crypto/sha512"
	"strings"
	"syscall/js"
)

func main() {
	done := make(chan struct{}, 0)
	js.Global().Set("wasmHash", js.FuncOf(convertToUpper))
	<-done
}

func convertToUpper(this js.Value, args []js.Value) interface{} {
	strings.ToUpper(args[0].String())
	return strings.ToUpper(args[0].String())
}
