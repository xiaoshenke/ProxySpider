#!/bin/bash

thrift --gen java:beans,generated_annotations=undated Proxy.thrift

