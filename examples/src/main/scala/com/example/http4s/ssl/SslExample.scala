package com.example.http4s
package ssl

import java.nio.file.Paths

import fs2._
import org.http4s.server.SSLKeyStoreSupport.StoreInfo
import org.http4s.server.{SSLKeyStoreSupport, ServerBuilder}
import org.http4s.server.middleware.HSTS
import org.http4s.util.StreamApp

import scala.concurrent.duration._

trait SslExample extends StreamApp {
  // TODO: Reference server.jks from something other than one child down.
  val keypath = Paths.get("../server.jks").toAbsolutePath().toString()

  def builder: ServerBuilder with SSLKeyStoreSupport

  def stream(args: List[String]) = builder
    .withSSL(StoreInfo(keypath, "password"), keyManagerPassword = "secure")
    .mountService(HSTS(ExampleService.service, 10.minutes), "/http4s")
    .bindHttp(8443)
    .serve
}
