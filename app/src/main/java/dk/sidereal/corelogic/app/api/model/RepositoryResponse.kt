package dk.sidereal.corelogic.app.api.model

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

data class RepositoryResponse(
    @SerializedName("total_count") val totalCount: Int = 0,
    @SerializedName("incomplete_results") val incompleteResults: Boolean = false,
    @SerializedName("items") val items: JsonArray = JsonArray()
)