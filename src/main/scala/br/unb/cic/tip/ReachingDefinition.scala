package br.unb.cic.tip

import br.unb.cic.tip.*
import br.unb.cic.tip.Stmt.*
import br.unb.cic.tip.Expression.*
import br.unb.cic.tip.Node.SimpleNode

import scala.collection.mutable

object ReachingDefinition {
  type DF = (Set[AssignmentStmt], Set[AssignmentStmt])
  type Result = mutable.HashMap[Stmt, DF]

  def run(program: Stmt): Result = {
    var explore = true

    val RD: Result = mutable.HashMap()
    var en = Set[AssignmentStmt]()
    var ex = Set[AssignmentStmt]()

    val cfg = flow(program)

    for (stmt <- blocks(program)) {
      RD(stmt) = (en, ex)
    }

    while (explore) {
      val lastRD = RD.clone()

      for (stmt <- blocks(program)) {
        en = entry(program, stmt, RD)
        ex = exit(program, stmt, RD)
        RD(stmt) = (en, ex)
      }
      explore = lastRD != RD
    }
    RD
  }

  def entry(program: Stmt, stmt: Stmt, RD: Result): Set[AssignmentStmt] = {
    var res = Set[AssignmentStmt]()
    for ((from, to) <- flow(program) if to == SimpleNode(stmt)) {
      from match {
        case SimpleNode(s) => res = RD(s)._2 union res
        case _             => throw new RuntimeException()
      }
    }
    res
  }

  def exit(program: Stmt, stmt: Stmt, RD: Result): Set[AssignmentStmt] =
    stmt match {
      case AssignmentStmt(id, exp) =>
        (RD(stmt)._1 diff kill(RD(stmt)._1, stmt)) union gen(stmt)
      case _ => RD(stmt)._1
    }

  def kill(pre: Set[AssignmentStmt], stmt: Stmt): Set[AssignmentStmt] =
    stmt match {
      case AssignmentStmt(id, exp) => pre.filter(_.name == id)
      case _                       => Set()
    }

  def gen(stmt: Stmt): Set[AssignmentStmt] = stmt match {
    case AssignmentStmt(id, exp) => Set(AssignmentStmt(id, exp))
    case _                       => Set()
  }
}
