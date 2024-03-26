package com.myspring.pro27.member.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro27.member.vo.MemberVO;


@Repository("memberDAO")
public class MemberDAOImpl  implements MemberDAO{

	
	@Autowired
	private SqlSession sqlSession;
	
	
	
	
	@Override
	public List<MemberVO> listMembers() throws DataAccessException {
		 List<MemberVO> memberList=sqlSession.selectList("mapper.member.selectAllMemberList");
	
		
		
		return memberList;
	}




	@Override
	public void addMember(MemberVO memberVO) throws DataAccessException {
		 sqlSession.insert("mapper.member.insertMember", memberVO);
		
	}




	@Override
	public void deleteMember(String id) throws DataAccessException {
		sqlSession.delete("mapper.member.deleteMember", id);
		
	}

	
	@Override
	public void updateMember(MemberVO memberVO) throws DataAccessException {
		sqlSession.delete("mapper.member.updateMember", memberVO);
		
	}




	@Override
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException {
		MemberVO vo=sqlSession.selectOne("mapper.member.loginById", memberVO);
		return vo;
	}
}
