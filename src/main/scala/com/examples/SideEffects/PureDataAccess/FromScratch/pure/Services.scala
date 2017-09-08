package com.examples.SideEffects.PureDataAccess.FromScratch.pure

import com.examples.SideEffects.PureDataAccess.commons.Domain._


object ServicesWithoutSugar {

  def join[F[_]](request: JoinRequest)(implicit S: Store[F]): F[JoinResponse] =
    S.doAndThen(S.getUser(request.uid)){ _ =>
      S.doAndThen(S.getGroup(request.gid)){ group =>
        if (group.must_approve) S.doAndThen(S.putJoin(request))(request => S.returns(Left(request)))
        else S.doAndThen(S.putMember(Member(None, request.uid, request.gid)))(member => S.returns(Right(member)))
      }
    }
}


object ServicesWithSomeSugar {
  import Store.Syntax._

  def join[F[_]: Store](request: JoinRequest): F[JoinResponse] =
    getUser(request.uid) flatMap { _ =>
      getGroup(request.gid) flatMap { group =>
        if (group.must_approve) left(putJoin(request))
        else right(putMember(Member(None, request.uid, request.gid)))
      }
    }
}


object ServicesWithForComprehensions {
  import Store.Syntax._

  def join[F[_]: Store](request: JoinRequest): F[JoinResponse] =
    for {
      _ <- getUser(request.uid)
      group <- getGroup(request.gid)
      result <-
      if (group.must_approve) left(putJoin(request))
      else right(putMember(Member(None, request.uid, request.gid)))
    } yield result
}


object Services {
  import Store.Syntax._

  def join[F[_]: Store](request: JoinRequest): F[JoinResponse] =
    for {
      _ <- getUser(request.uid)
      group <- getGroup(request.gid)
      result <- cond(
        test = group.must_approve,
        left = putJoin(request),
        right = putMember(Member(None, request.uid, request.gid)))
    } yield result
}
