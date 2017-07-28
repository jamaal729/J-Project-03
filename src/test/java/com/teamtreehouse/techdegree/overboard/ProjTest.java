package com.teamtreehouse.techdegree.overboard;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;

import com.teamtreehouse.techdegree.overboard.model.Answer;
import com.teamtreehouse.techdegree.overboard.model.Board;
import com.teamtreehouse.techdegree.overboard.model.Question;
import com.teamtreehouse.techdegree.overboard.model.User;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjTest {

    private Board testBoard;

    private User qUser;
    private User aUser;
    private User oUser;

    private Question question;
    private Answer answer;

    private String votingExceptionMessage;
    private String answerAcceptanceExceptionMessage;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        testBoard = new Board("Java Unit Testing");

        qUser = testBoard.createUser("questioner");
        aUser = testBoard.createUser("answerer");
        oUser = testBoard.createUser("otherUser");

        question = qUser.askQuestion("question");
        answer = aUser.answerQuestion(question, "answer");

        answerAcceptanceExceptionMessage = String.format("Only %s can accept this answer as it is their question",
                qUser.getName());
        votingExceptionMessage = "You cannot vote for yourself!";
    }

    @Test
    public void Test01_increaseQuestionerReputationByFivePointsForUpvotedQuestion() {

        oUser.upVote(question);
        assertEquals(5, qUser.getReputation());
    }

    @Test
    public void Test02_increaseAnswererReputationByTenPointsForUpvotedAnswer() {

        qUser.upVote(answer);
        assertEquals(10, aUser.getReputation());
    }

    @Test
    public void Test03_decreaseAnswererReputationByOnePointForDownvotedAnswer() {

        qUser.downVote(answer);
        assertEquals(-1, aUser.getReputation());
    }

    @Test
    public void Test04_increaseAnswererReputationByFifteenPointsForAcceptedAnswer() {

        qUser.acceptAnswer(answer);
        assertEquals(15, aUser.getReputation());
    }

    @Test
    public void Test05_votingUpNotAllowedOnQuestionByOriginalAuthor() throws Exception {

        thrown.expect(VotingException.class);
        thrown.expectMessage(votingExceptionMessage);

        qUser.upVote(question);
    }

    @Test
    public void Test06_votingDownNotAllowedOnQuestionByOriginalAuthor() throws Exception {

        thrown.expect(VotingException.class);
        thrown.expectMessage(votingExceptionMessage);

        qUser.downVote(question);
    }

    @Test
    public void Test07_votingUpNotAllowedOnAnswerByOriginalAuthor() throws Exception {

        thrown.expect(VotingException.class);
        thrown.expectMessage(votingExceptionMessage);

        aUser.upVote(answer);
    }

    @Test
    public void Test08_votingDownNotAllowedOnAnswerByOriginalAuthor() throws Exception {

        thrown.expect(VotingException.class);
        thrown.expectMessage(votingExceptionMessage);

        aUser.downVote(answer);
    }

    @Test
    public void Test09_onlyOriginalQuestionerCanAcceptAnAnswer() throws Exception {

        qUser.acceptAnswer(answer);
        assertTrue(answer.isAccepted());
    }

    @Test
    public void Test10_userOtherThanQuestionerCannotAcceptAnAnswer() throws Exception {

        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage(answerAcceptanceExceptionMessage);

        aUser.acceptAnswer(answer);
    }
}
