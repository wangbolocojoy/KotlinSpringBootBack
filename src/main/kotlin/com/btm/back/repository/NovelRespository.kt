package com.btm.back.repository

import com.btm.back.dto.Novel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
@Component
interface NovelRespository : JpaRepository<Novel, String>, JpaSpecificationExecutor<Any> {
    @Query("select st from Novel st where st.novel_name like %?1%")
    fun findByNovel_nameLike(name: String?): List<Novel>

    //    @Query("select st from Novel st where st.novel_name")
//    Novel findByNovel_name(String name);
    @Query("select st from Novel st where st.novel_type=:novel_type")
    fun findAllByNovel_type(@Param("novel_type") type: Int, pageable: Pageable): Page<Novel>
}
