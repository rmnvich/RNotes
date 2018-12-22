package rmnvich.apps.notes.domain.interactors

import rmnvich.apps.notes.domain.entity.Note

class Response(val status: Status, val data: List<Note>?, val error: Throwable?) {

    companion object {

        fun onLoading(): Response {
            return Response(Status.LOADING, null, null)
        }

        fun onSuccess(data: List<Note>): Response {
            return Response(Status.SUCCESS, data, null)
        }

        fun onError(error: Throwable): Response {
            return Response(Status.ERROR, null, error)
        }

    }
}