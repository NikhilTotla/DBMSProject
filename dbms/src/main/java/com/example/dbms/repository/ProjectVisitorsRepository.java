package com.example.dbms.repository;

import com.example.dbms.entity.ProjectVisitors;
import com.example.dbms.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProjectVisitorsRepository extends JpaRepository<ProjectVisitors, Integer> {

    List<ProjectVisitors> findByProjectId(Integer projectId);

    @Query("SELECT v FROM Visitor v JOIN ProjectVisitors pv ON v.id = pv.visitorId WHERE pv.projectId = :projectId")
    List<Visitor> findVisitorsByProjectId(@Param("projectId") Integer projectId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProjectVisitors pv WHERE pv.projectId = :projectId AND pv.visitorId = :visitorId")
    void deleteByProjectIdAndServedBy(@Param("projectId") Integer projectId, @Param("visitorId") Integer visitorId);

    @Query("SELECT pv FROM ProjectVisitors pv WHERE pv.projectId = :projectId AND pv.visitorId = :visitorId")
    ProjectVisitors findByProjectIdAndServedBy(@Param("projectId") Integer projectId, @Param("visitorId") Integer visitorId);
}
