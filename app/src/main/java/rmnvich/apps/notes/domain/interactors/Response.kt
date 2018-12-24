package rmnvich.apps.notes.domain.interactors

data class Response<out T>(val status: Status, val data: T?, val error: Throwable?) {

    companion object {

        fun <T> onLoading(): Response<T> {
            return Response(Status.LOADING, null, null)
        }

        fun <T> onSuccess(data: T?): Response<T> {
            return Response(Status.SUCCESS, data, null)
        }

        fun <T> onError(error: Throwable): Response<T> {
            return Response(Status.ERROR, null, error)
        }

    }
}