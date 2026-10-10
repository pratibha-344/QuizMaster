<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="page-heading"><p class="eyebrow">BUILD YOUR MOMENTUM</p><h1>Find your next quiz</h1><p class="muted">Pick a topic, set aside a few minutes, and see what you know.</p></section>
<div class="quiz-grid quiz-grid-all">
  <c:forEach var="quiz" items="${quizzes}">
    <article class="quiz-card">
      <div class="quiz-card-top"><span class="tag"><c:out value="${quiz.questionCount}" /> questions</span><span class="time-label"><c:out value="${quiz.timeLimitMinutes}" /> min</span></div>
      <h3><c:out value="${quiz.title}" /></h3><p class="muted"><c:out value="${quiz.description}" /></p>
      <div class="quiz-card-foot"><span class="muted"><c:out value="${quiz.questionCount}" /> multiple-choice questions</span>
        <c:choose><c:when test="${quiz.questionCount gt 0}"><form action="<c:url value='/student/quiz/start' />" method="post"><input type="hidden" name="_csrf" value="<c:out value='${csrfToken}' />"><input type="hidden" name="quizId" value="<c:out value='${quiz.id}' />"><button class="button button-primary button-small" type="submit">Start quiz →</button></form></c:when><c:otherwise><button class="button button-disabled button-small" type="button" disabled>Coming soon</button></c:otherwise></c:choose>
      </div>
    </article>
  </c:forEach>
  <c:if test="${empty quizzes}"><div class="empty-state"><strong>No published quizzes</strong><p>Your quiz list will appear here when an administrator publishes one.</p></div></c:if>
</div>
<section class="note-strip"><strong>Before you begin</strong><span>Your timer starts when you select Start. You can submit at any time, and unanswered questions count as incorrect.</span></section>
<%@ include file="includes/foot.jspf" %>
