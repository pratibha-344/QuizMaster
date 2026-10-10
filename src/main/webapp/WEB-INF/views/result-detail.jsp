<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="result-hero">
  <div class="result-orb"><strong><c:out value="${result.percentage}" /><small>%</small></strong></div>
  <p class="eyebrow">QUIZ COMPLETE</p><h1>Nice work showing up.</h1><p class="muted">Your result for <strong><c:out value="${result.quizTitle}" /></strong> is ready.</p>
  <div class="result-facts"><span><strong><c:out value="${result.score}" /> / <c:out value="${result.totalQuestions}" /></strong>correct answers</span><span><strong><c:out value="${result.durationSeconds / 60}" /> min</strong>time used</span><span><strong><c:out value="${result.submittedAt}" /></strong>submitted</span></div>
  <div class="button-row"><a class="button button-primary" href="<c:url value='/student/quizzes' />">Try another quiz</a><a class="button button-outline" href="<c:url value='/student/results' />">View all results</a></div>
</section>
<%@ include file="includes/foot.jspf" %>
