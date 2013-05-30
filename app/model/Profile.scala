package model

case class Profile(age: Int, gender: String, attributes: List[String], raw: String) {
  def name = attributes.mkString(", ")
}