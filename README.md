# mu test

## How to run

Run `make`. Then run `make run-server`, and in a different terminal,
`make run-client`.

## Expected behavior

The client prints `Good bye!`.

## Actual behavior before the fix

```
$ make run-client
go run gotest/cmd/*.go
panic: rpc error: code = Unimplemented desc = Method not found: rpctest.pkg.Greeter/SayHello

goroutine 1 [running]:
main.main()
	/Users/sander/src/exp/rpctest/gotest/cmd/main.go:23 +0x23b
exit status 2
make: *** [run-client] Error 1
```
