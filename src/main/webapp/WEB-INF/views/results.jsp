<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="page-heading"><p class="eyebrow">EVERY ATTEMPT IS PROGRESS</p><h1>Your quiz history</h1><p class="muted">All your scores, together in one place.</p></section>
<c:choose><c:when test="${empty results}"><div class="empty-state"><strong>Nothing here yet</strong><p>Finish your first quiz and you will find the result here.</p><a class="button button-primary" href="<c:url value='/student/quizzes' />">Browse quizzes</a></div></c:when><c:otherwise>
<div class="table-wrap"><table><thead><tr><th>Quiz</th><th>Score</th><th>Percentage</th><th>Time taken</th><th>Date completed</th><th></th></tr></thead><tbody>
<c:forEach var="result" items="${results}"><tr><td class="table-primary"><c:out value="${result.quizTitle}" /></td><td><c:out value="${result.score}" /> / <c:out value="${result.totalQuestions}" /></td><td><span class="score-pill"><c:out value="${result.percentage}" />%</span></td><td><c:out value="${result.durationSeconds / 60}" /> min</td><td><c:out value="${result.submittedAt}" /></td><td><a class="text-link" href="<c:url value='/student/results/view?id=${result.id}' />">Details</a></td></tr></c:forEach>
</tbody></table></div></c:otherwise></c:choose>
<%@ include file="includes/foot.jspf" %>
