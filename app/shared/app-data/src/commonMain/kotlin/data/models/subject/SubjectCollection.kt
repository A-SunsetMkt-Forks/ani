/*
 * Copyright (C) 2024 OpenAni and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license, which can be found at the following link.
 *
 * https://github.com/open-ani/ani/blob/main/LICENSE
 */

package me.him188.ani.app.data.models.subject

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.him188.ani.app.data.models.episode.EpisodeCollection
import me.him188.ani.app.data.models.episode.episode
import me.him188.ani.datasources.api.topic.UnifiedCollectionType

/**
 * 包含用户的观看进度的条目及其剧集信息
 */
@Immutable
@Serializable
data class SubjectCollection(
    // TODO: this is very shit, do not store them all together
    val info: SubjectInfo,
    val episodes: List<EpisodeCollection>, // must be sorted by sort
    val collectionType: UnifiedCollectionType,
    /**
     * 如果未收藏, 此属性为 `null`
     */
    val selfRatingInfo: SelfRatingInfo,
) {
    val displayName: String get() = info.displayName
    val subjectId: Int get() = info.id

    override fun toString(): String = "SubjectCollectionItem($displayName)"

    @Transient
    val airingInfo: SubjectAiringInfo = SubjectAiringInfo.computeFromEpisodeList(
        episodes.map { it.episode },
        airDate = info.airDate,
    )

    @Transient
    val progressInfo: SubjectProgressInfo = SubjectProgressInfo.calculate(this)

    companion object {
        val Empty = SubjectCollection(
            SubjectInfo.Empty,
            emptyList(),
            UnifiedCollectionType.NOT_COLLECTED,
            SelfRatingInfo.Empty,
        )
    }
}

/**
 * 是否已经开播了第一集
 */
val SubjectCollection.hasStarted get() = airingInfo.hasStarted
