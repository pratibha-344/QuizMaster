<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="quiz-intro">
  <div><p class="eyebrow">QUIZ IN PROGRESS</p><h1><c:out value="${quiz.title}" /></h1><p class="muted"><c:out value="${questions.size()}" /> questions · Your progress saves when you submit.</p></div>
  <div class="timer-card" id="quiz-timer" data-remaining="<c:out value='${remainingSeconds}' />"><span class="timer-caption">TIME REMAINING</span><strong id="timer-value">--:--</strong></div>
</section>
<form id="quiz-form" class="question-stack" method="post" action="<c:url value='/student/quiz/submit' />">
  <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}' />"><input type="hidden" name="quizId" value="<c:out value='${quiz.id}' />">
  <c:forEach var="question" items="${questions}" varStatus="loop">
    <fieldset class="question-card"><legend><span class="question-number"><c:out value="${loop.count}" /></span><span><c:out value="${question.text}" /></span></legend>
      <div class="options-grid">
        <label class="option"><input type="radio" name="q_<c:out value='${question.id}' />" value="A"><span class="option-key">A</span><span><c:out value="${question.optionA}" /></span></label>
        <label class="option"><input type="radio" name="q_<c:out value='${question.id}' />" value="B"><span class="option-key">B</span><span><c:out value="${question.optionB}" /></span></label>
        <label class="option"><input type="radio" name="q_<c:out value='${question.id}' />" value="C"><span class="option-key">C</span><span><c:out value="${question.optionC}" /></span></label>
        <label class="option"><input type="radio" name="q_<c:out value='${question.id}' />" value="D"><span class="option-key">D</span><span><c:out value="${question.optionD}" /></span></label>
      </div>
    </fieldset>
  </c:forEach>
  <div class="submit-bar"><span class="muted">Take a breath. Review your choices, then submit.</span><button class="button button-primary" type="submit">Submit quiz <span aria-hidden="true">→</span></button></div>
</form>
<%@ include file="includes/foot.jspf" %>
