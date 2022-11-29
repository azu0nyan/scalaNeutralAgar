package NNAgar.game

import java.awt.{Color, Graphics2D}
import scala.util.Random

object GameModel {

  case class GameParams(initialSize: Double = 40d,
                        tickTime: Double = 1.0 / 60.0,
                        sizePerFood: Double = 30.0,
                        initialFood: Int = 30,
                        maxFood: Int = 30,
                        foodPerTick: Double = 0.2,
                        area: V2 = V2(1000, 1000),
                        dSizePerTick: Double = 0.02,

                        initialObstacles: Int = 4,
                        obstacleMin:V2 = V2(100, 100),
                        obstacleMax:V2 = V2(550, 250),

                        angleSpeedMax: Double = 12 * Math.PI ,
                        speed: Double => Double = size => math.max(10, 400 - 400 * size / 1000d),
                        seed: Int = new Random().nextInt()){
    def obstacleDelta:V2 = obstacleMax - obstacleMin
  }

  case class Player(id: Int,
                    pos: V2, controlDir: V2,
                    size: Double,
                    spawnedAt: Int = 0,
                    deadAt: Option[Int] = None,
                    lookDir: V2 = V2(1, 0),
                    eatenEnemy: Double = 0,
                    eatenFood: Double = 0,
                    distanceTraveled: Double = 0) {

    def rad: Double = math.sqrt(size)

    def contains(v: V2): Boolean = (pos - v).length < rad

    def intersects(ot: Player): Boolean = (pos - ot.pos).length < rad

    def alive: Boolean = deadAt.isEmpty

    def aliveSec(g: Game): Double = (deadAt.getOrElse(g.tick) - spawnedAt) * g.params.tickTime


  }

  case class Obstacle(min:V2, max:V2) {
    def width: Double = max.x - min.x
    def height: Double = max.y - min.y
//    def intersection(origin:V2, end: V2):Option[V2] = {
//      sides.flatMap{case (s, e) => Helpers.segmentIntersection(s,e, origin, end)}
//    }
//
//    def sides:Seq[(V2, V2)]= Seq(
//      min,
//      V2(min.x, max.y),
//      max,
//      V2(max.x, min.y)
//    )

  }

  case class Game(params: GameParams = GameParams(),
                  obstacles: Seq[Obstacle] = Seq(),
                  alivePlayers: Seq[Player] = Seq(),
                  food: Seq[V2] = Seq(),
                  tick: Int = 0,
                  deadPlayers: Seq[Player] = Seq()){

    def player(id:Int): Player = alivePlayers.find(_.id == id).getOrElse(deadPlayers.find(_.id == id).get)
    def playerCount:Int = alivePlayers.size + deadPlayers.size
  }




}
