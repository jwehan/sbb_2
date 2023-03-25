package com.mypage.sbb;

import com.mypage.sbb.answer.Answer;
import com.mypage.sbb.answer.AnswerRepository;
import com.mypage.sbb.question.Question;
import com.mypage.sbb.question.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;

	@BeforeEach
	void beforeEach() {
		answerRepository.deleteAll();
		answerRepository.clearAutoIncrement();
		questionRepository.deleteAll();
		questionRepository.clearAutoIncrement();

		Question q1 = new Question();
		q1.setSubject("질문1");
		q1.setContent("질문1_내용");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("질문2");
		q2.setContent("질문2_내용");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);

		Answer a1 = new Answer();
		a1.setContent("답변1");
		q2.addAnswer(a1);
		a1.setCreateDate(LocalDateTime.now());
		answerRepository.save(a1);
	}

	@Test
	@DisplayName("데이터 저장")
	void t001() {
		Question q = new Question();
		q.setSubject("질문3");
		q.setContent("질문3_내용");
		q.setCreateDate(LocalDateTime.now());
		questionRepository.save(q);

		assertEquals("질문3", questionRepository.findById(3).get().getSubject());
	}

	@Test
	@DisplayName("findAll")
	void t002() {
		List<Question> all = questionRepository.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("질문1", q.getSubject());
	}
	@Test
	@DisplayName("findById")
	void t003() {
		Optional<Question> oq = questionRepository.findById(1);

		if (oq.isPresent()) {
			Question q = oq.get();
			assertEquals("질문1", q.getSubject());
		}
	}
	@Test
	@DisplayName("findBySubject")
	void t004() {
		Question q = questionRepository.findBySubject("질문1");
		assertEquals(1, q.getId());
	}
	@Test
	@DisplayName("findBySubjectAndContent")
	void t005() {
		Question q = questionRepository.findBySubjectAndContent(
				"질문1", "질문1_내용"
		);
		assertEquals(1, q.getId());
	}
	@Test
	@DisplayName("findBySubjectLike")
	void t006() {
		List<Question> qList = questionRepository.findBySubjectLike("질%");
		Question q = qList.get(0);
		assertEquals("질문1", q.getSubject());
	}
	@Test
	@DisplayName("데이터 수정하기")
	void t007() {
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정1");
		questionRepository.save(q);
	}
	@Test
	@DisplayName("데이터 삭제하기")
	void t008() {
		assertEquals(2, questionRepository.count());
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		questionRepository.delete(q);
		assertEquals(1, questionRepository.count());
	}

	@Test
	@DisplayName("답변 데이터 생성 후 저장하기")
	void t009() {
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		Answer a = new Answer();
		a.setContent("답변 자동생성");
		a.setQuestion(q);
		a.setCreateDate(LocalDateTime.now());
		answerRepository.save(a);
	}
	@Test
	@DisplayName("답변 조회하기")
	void t010() {
		Optional<Answer> oa = answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
	}

	@Transactional
	@Rollback(false)
	@Test
	@DisplayName("질문에 달린 답변 찾기")
	void t011() {
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("답변1", answerList.get(0).getContent());
	}
}