<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="page-heading"><p class="eyebrow">STUDENT PROGRESS</p><h1>Quiz results</h1><p class="muted">The latest 300 saved quiz attempts across your students.</p></section>
<c:choose><c:when test="${empty results}"><div class="empty-state"><strong>No results yet</strong><p>Completed student quizzes will appear here.</p></div></c:when><c:otherwise>
<div class="table-wrap"><table><thead><tr><th>Student</th><th>Quiz</th><th>Score</th><th>Percentage</th><th>Time</th><th>Submitted</th></tr></thead><tbody>
<c:forEach var="result" items="${results}"><tr><td class="table-primary"><c:out value="${result.studentName}" /></td><td><c:out value="${result.quizTitle}" /></td><td><c:out value="${result.score}" /> / <c:out value="${result.totalQuestions}" /></td><td><span class="score-pill"><c:out value="${result.percentage}" />%</span></td><td><c:out value="${result.durationSeconds / 60}" /> min</td><td><c:out value="${result.submittedAt}" /></td></tr></c:forEach>
</tbody></table></div></c:otherwise></c:choose>
<%@ include file="includes/foot.jspf" %>
