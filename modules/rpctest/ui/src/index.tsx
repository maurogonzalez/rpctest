import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./App";
import * as serviceWorker from "./serviceWorker";

import { HelloRequest } from "./proto/protocol_pb.js";
import { GreeterClient } from "./proto/ProtocolServiceClientPb";

ReactDOM.render(<App />, document.getElementById("root"));

const req = new HelloRequest();
req.setName("hello world");

const client = new GreeterClient("http://localhost:8080", null, null);
client.sayHello(req, {}, (err, res) => {
  if (err) console.error("Got error:", err.code, err.message);
  else console.log("Got response:", res.getMessage());
});

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
