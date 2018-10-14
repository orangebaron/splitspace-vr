package ga.hhfed.masochistsplitspace

class Point (var x: Float, var y: Float) {
    operator fun plus (p: Point) = Point(this.x + p.x, this.y + p.y)
    operator fun minus (p: Point) = Point(this.x - p.x, this.y - p.y)
    operator fun times (p: Point) = Point(this.x * p.x, this.y * p.y)
    operator fun times (a: Float) = Point(this.x * a, this.y * a)
    operator fun div (p: Point) = Point(this.x / p.x, this.y / p.y)
    operator fun div (a: Float) = Point(this.x / a, this.y / a)
    val size
        get() = Math.sqrt(Math.pow(this.x.toDouble(),2.0)+Math.pow(this.y.toDouble(),2.0)).toFloat()
    fun dist (p: Point) = (this-p).size
}
