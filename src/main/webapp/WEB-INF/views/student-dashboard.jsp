<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="welcome-row">
  <div><p class="eyebrow">STUDENT SPACE</p><h1>Good to see you, <c:out value="${currentUser.fullName}" />.</h1><p class="muted">A little practice today goes a long way.</p></div>
  <a class="button button-primary" href="<c:url value='/student/quizzes' />">Explore quizzes <span aria-hidden="true">→</span></a>
</section>
<section class="stat-grid">
  <article class="stat-card"><span class="stat-label">Quizzes available</span><strong><c:out value="${quizzes.size()}" /></strong><span class="stat-note">Ready when you are</span></article>
  <article class="stat-card"><span class="stat-label">Attempts completed</span><strong><c:out value="${results.size()}" /></strong><span class="stat-note">Every attempt counts</span></article>
  <article class="stat-card"><span class="stat-label">Average score</span><strong><c:out value="${average}" />%</strong><span class="stat-note">Across all your results</span></article>
</section>
<section class="content-section">
  <div class="section-heading"><div><p class="eyebrow">PICK UP A NEW SKILL</p><h2>Available quizzes</h2></div><a class="text-link" href="<c:url value='/student/quizzes' />">All quizzes <span aria-hidden="true">→</span></a></div>
  <div class="quiz-grid">
    <c:forEach var="quiz" items="${quizzes}" end="2">
      <article class="quiz-card"><div class="quiz-card-top"><span class="tag"><c:out value="${quiz.questionCount}" /> questions</span><span class="time-label"><c:out value="${quiz.timeLimitMinutes}" /> min</span></div>
        <h3><c:out value="${quiz.title}" /></h3><p class="muted"><c:out value="${quiz.description}" /></p>
        <form action="<c:url value='/student/quiz/start' />" method="post"><input type="hidden" name="_csrf" value="<c:out value='${csrfToken}' />"><input type="hidden" name="quizId" value="<c:out value='${quiz.id}' />"><button class="button button-outline" type="submit">Start quiz <span aria-hidden="true">→</span></button></form>
      </article>
    </c:forEach>
    <c:if test="${empty quizzes}"><div class="empty-state"><strong>No quizzes just yet</strong><p>Check back when your instructor publishes one.</p></div></c:if>
  </div>
</section>
<section class="content-section">
  <div class="section-heading"><div><p class="eyebrow">YOUR LATEST WORK</p><h2>Recent results</h2></div><a class="text-link" href="<c:url value='/student/results' />">View history <span aria-hidden="true">→</span></a></div>
  <c:choose><c:when test="${empty results}"><div class="empty-state"><strong>Your first score is waiting</strong><p>Complete a quiz and your result will show here.</p></div></c:when><c:otherwise>
  <div class="table-wrap"><table><thead><tr><th>Quiz</th><th>Score</th><th>Completed</th><th></th></tr></thead><tbody>
    <c:forEach var="result" items="${results}" end="3"><tr><td class="table-primary"><c:out value="${result.quizTitle}" /></td><td><c:out value="${result.score}" /> / <c:out value="${result.totalQuestions}" /></td><td><c:out value="${result.submittedAt}" /></td><td><a class="text-link" href="<c:url value='/student/results/view?id=${result.id}' />">View</a></td></tr></c:forEach>
  </tbody></table></div></c:otherwise></c:choose>
</section>
<%@ include file="includes/foot.jspf" %>
