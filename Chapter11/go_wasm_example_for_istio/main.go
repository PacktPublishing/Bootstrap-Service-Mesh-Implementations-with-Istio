package main

import (
	"github.com/tetratelabs/proxy-wasm-go-sdk/proxywasm"
	"github.com/tetratelabs/proxy-wasm-go-sdk/proxywasm/types"
)

func main() {
	proxywasm.SetVMContext(&vmContext{})
}

type vmContext struct {
	types.DefaultVMContext
}

func (*vmContext) NewPluginContext(contextID uint32) types.PluginContext {
	return &myPluginContext{}
}

type myPluginContext struct {
	types.DefaultPluginContext
	contextID uint32
}

func (*myPluginContext) NewHttpContext(contextID uint32) types.HttpContext {
	return &httpHeaders{contextID: contextID}
}

type httpHeaders struct {
	types.DefaultHttpContext
	contextID uint32
}

func (ctx *httpHeaders) OnHttpRequestHeaders(numHeaders int, endOfStream bool) types.Action {
	if err := proxywasm.AddHttpRequestHeader("X-Chapter", "Chapter11"); err != nil {
		proxywasm.LogCritical("failed to set request header: X-ChapterName")

	}
	proxywasm.LogInfof("added custom header to request")
	return types.ActionContinue
}

func (ctx *httpHeaders) OnHttpStreamDone() {
	proxywasm.LogInfof("%d finished", ctx.contextID)
}
