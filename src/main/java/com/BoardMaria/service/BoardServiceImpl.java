package com.BoardMaria.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BoardMaria.dao.BoardDAO;
import com.BoardMaria.dto.BoardVO;
import com.BoardMaria.dto.FileVO;
import com.BoardMaria.dto.LikeVO;
import com.BoardMaria.dto.replyVO;


@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	BoardDAO dao; //인터페이스 자체를 호출함.
	
	
	//게시물 목록 보기
	@Override
	public List<BoardVO> list(int startPoint,int postNum,String keyword) {
		return dao.list(startPoint,postNum,keyword);
	}
	
	//게시물 등록
	@Override
	public void write(BoardVO board) {
		dao.write(board);
	}
	
	//파일 업로드 정보 등록
	@Override
	public void fileInfoRegistry(Map<String, Object> fileInfo) throws Exception {
		dao.fileInfoRegistry(fileInfo);
	}

	// 다운로드를 위한 파일 정보 보기
	@Override
	public FileVO fileInfo(int fileseqno) throws Exception {
		return dao.fileInfo(fileseqno);
	}

	// 게시글 내에서 업로드된 파일 목록 보기
	@Override
	public List<FileVO> fileListView(int seqno) throws Exception {
		return dao.fileListView(seqno);
	}

	// 게시물 수정에서 파일 삭제
	@Override
	public void deleteFileList(Map<String, Object> data) throws Exception {
		dao.deleteFileList(data);
	}

	// 게시물 삭제에서 파일 삭제를 위한 파일 목록 가져 오기
	@Override
	public List<FileVO> deleteFileOnBoard(int seqno) throws Exception {
		return dao.deleteFileOnBoard(seqno);
	}

	// 회원 탈퇴 시 회원이 업로드한 파일 정보 가져 오기
	@Override
	public List<FileVO> fileInfoByUserid(String userid) throws Exception {
		return dao.fileInfoByUserid(userid);
	}

	//게시물 상세 보기
	@Override
	public BoardVO view(int seqno) {
		return dao.view(seqno);
	}

	//조회수 등록
	@Override
	public void hitno(BoardVO board) {
		dao.hitno(board);
	}

	//게시물 수정
	@Override
	public void modify(BoardVO board) {
		dao.modify(board);
	}

	//게시물 삭제
	@Override
	public void delete(int seqno) {
		dao.delete(seqno);
	}

	//게시물 전체 갯수
	@Override
	public int getTotalCount(String keyword) {
		
		return dao.getTotalCount(keyword);
	}
	//게시물 이전 보기
	@Override
	public int pre_seqno(int seqno, String keyword) {
		return dao.pre_seqno(seqno, keyword);
	}
	//게시물 다음 보기
	@Override
	public int next_seqno(int seqno, String keyword) {
		return dao.next_seqno(seqno, keyword);
	}
	
	//좋아요/싫어요 확인 가져 오기
		@Override
		public LikeVO likeCheckView(int seqno,String userid) throws Exception {
			return dao.likeCheckView(seqno, userid);
		}
		
		//좋아요/싫어요 갯수 수정하기
		@Override
		public void boardLikeUpdate(int seqno, int likecnt, int dislikecnt) throws Exception {
			dao.boardLikeUpdate(seqno, likecnt, dislikecnt);
		}
		
		//좋아요/싫어요 확인 등록하기
		@Override
		public void likeCheckRegistry(Map<String,Object> map) throws Exception {
			dao.likeCheckRegistry(map);
		}
		
		//좋아요/싫어요 확인 수정하기
		@Override
		public void likeCheckUpdate(Map<String,Object> map) throws Exception {
			dao.likeCheckUpdate(map);
		}
	
	//댓글 보기
		@Override
		public List<replyVO> replyView(replyVO reply) throws Exception{
			return dao.replyView(reply);
		}
		
		//댓글 수정
		@Override
		public void replyUpdate(replyVO reply) throws Exception{
			dao.replyUpdate(reply);
		}
		
		//댓글 등록
		@Override
		public void replyRegistry(replyVO reply) throws Exception{
			dao.replyRegistry(reply);
		}
		
		//댓글 삭제
		@Override
		public void replyDelete(replyVO reply) throws Exception{
			dao.replyDelete(reply);
		}

}
