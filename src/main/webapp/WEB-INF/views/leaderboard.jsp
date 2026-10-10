<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="page-heading"><p class="eyebrow">CELEBRATE THE EFFORT</p><h1>Leaderboard</h1><p class="muted">Ranked by average score across completed quiz attempts.</p></section>
<c:choose><c:when test="${empty entries}"><div class="empty-state"><strong>The board is wide open</strong><p>Complete a quiz to earn the first spot.</p></div></c:when><c:otherwise>
<div class="table-wrap"><table><thead><tr><th>Rank</th><th>Student</th><th>Attempts</th><th>Average score</th><th>Best score</th></tr></thead><tbody>
<c:forEach var="entry" items="${entries}" varStatus="loop"><tr><td><span class="rank-number"><c:out value="${loop.count}" /></span></td><td class="table-primary"><c:out value="${entry.studentName}" /><c:if test="${entry.userId eq currentUser.id}"><span class="you-label">YOU</span></c:if></td><td><c:out value="${entry.attempts}" /></td><td><span class="score-pill"><c:out value="${entry.averagePercentage}" />%</span></td><td><c:out value="${entry.bestPercentage}" />%</td></tr></c:forEach>
</tbody></table></div></c:otherwise></c:choose>
<%@ include file="includes/foot.jspf" %>
