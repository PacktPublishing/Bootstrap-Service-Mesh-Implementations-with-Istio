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

func (ctx *httpHeaders) OnHttpResponseHeaders(numHeaders int, endOfStream bool) types.Action {
	if err := proxywasm.AddHttpResponseHeader("X-ChapterName", "ExtendingEnvoy"); err != nil {
		proxywasm.LogCritical("failed to set response header: X-ChapterName")

	}
	proxywasm.LogInfof("added custom header to response")
	return types.ActionContinue
}

func (ctx *httpHeaders) OnHttpStreamDone() {
	proxywasm.LogInfof("%d finished", ctx.contextID)
}
