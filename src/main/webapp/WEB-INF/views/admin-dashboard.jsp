<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="welcome-row"><div><p class="eyebrow">ADMINISTRATION</p><h1>Good morning, <c:out value="${currentUser.fullName}" />.</h1><p class="muted">A quick look at your QuizMaster community.</p></div><a class="button button-primary" href="<c:url value='/admin/quizzes/new' />">Create a quiz <span aria-hidden="true">+</span></a></section>
<section class="stat-grid">
  <article class="stat-card"><span class="stat-label">Registered students</span><strong><c:out value="${studentCount}" /></strong><span class="stat-note">Student accounts</span></article>
  <article class="stat-card"><span class="stat-label">Quizzes</span><strong><c:out value="${quizCount}" /></strong><span class="stat-note">Published and drafts</span></article>
  <article class="stat-card"><span class="stat-label">Quiz attempts</span><strong><c:out value="${resultCount}" /></strong><span class="stat-note">Results saved</span></article>
</section>
<section class="content-section">
  <div class="section-heading"><div><p class="eyebrow">RECENT ACTIVITY</p><h2>Latest results</h2></div><a class="text-link" href="<c:url value='/admin/results' />">All results →</a></div>
  <c:choose><c:when test="${empty recentResults}"><div class="empty-state"><strong>No attempts yet</strong><p>Student quiz results will appear here.</p></div></c:when><c:otherwise>
  <div class="table-wrap"><table><thead><tr><th>Student</th><th>Quiz</th><th>Score</th><th>Submitted</th></tr></thead><tbody>
    <c:forEach var="result" items="${recentResults}"><tr><td class="table-primary"><c:out value="${result.studentName}" /></td><td><c:out value="${result.quizTitle}" /></td><td><c:out value="${result.score}" /> / <c:out value="${result.totalQuestions}" /></td><td><c:out value="${result.submittedAt}" /></td></tr></c:forEach>
  </tbody></table></div></c:otherwise></c:choose>
</section>
<%@ include file="includes/foot.jspf" %>
