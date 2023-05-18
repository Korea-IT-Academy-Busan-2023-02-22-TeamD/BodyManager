package com.groupd.bodymanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.groupd.bodymanager.entity.BoardEntity;
import com.groupd.bodymanager.entity.resultSet.BoardListResultSet;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, String>{
    public BoardEntity findByBoardNumber(int boardNumber); 

    @Query(
        value = 
        "SELECT "
        +"B.board_number AS boardNumber, "
        +"M.manager_email AS boardWriterEmail, "
        +"B.board_writer_nickname AS boardWriterNickname, "
        +"B.board_title AS title, "
        +"B.board_content AS boardContent, "
        +"B.board_image_url AS boardImageUrl, "
        +"B.view_count AS viewCount, "
        +"B.board_write_datetime AS boardWriteDatetime "
        +"From board B, manager M, user U "
        +"WHERE B.board_writer_email = M.manager_email "
        +"AND M.manager_email = U.user_email "
        +"ORDER BY B.board_number DESC; ",
        nativeQuery = true  
    )
    public List<BoardListResultSet> getList();
}