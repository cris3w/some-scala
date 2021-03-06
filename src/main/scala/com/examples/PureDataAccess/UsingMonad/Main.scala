package com.examples.PureDataAccess.UsingMonad

import scala.util.Try
import scalaz.Id.Id

import com.examples.PureDataAccess.commons.Domain._
import com.examples.PureDataAccess.UsingMonad.impure._


// example based on Juan Manuel Serrano's meetup:
// https://www.youtube.com/watch?v=lTS_N-IEGUI&t=3154s&list=WL&index=2


object Main extends App {
  import com.examples.PureDataAccess.UsingMonad.pure.Services

  implicit val _ = IdStore

  println(Try(Services.join[Id](JoinRequest(None, 1, 1))))
}
