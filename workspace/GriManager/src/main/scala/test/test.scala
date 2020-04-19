package test

object test extends App{
  println("/user/xhb/*".split("/").reverse.filter(x=>(!x.equals("*"))).head)
}
