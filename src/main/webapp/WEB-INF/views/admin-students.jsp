<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="page-heading"><p class="eyebrow">YOUR COMMUNITY</p><h1>Students</h1><p class="muted">Student accounts registered on QuizMaster.</p></section>
<c:choose><c:when test="${empty students}"><div class="empty-state"><strong>No student accounts yet</strong><p>New student registrations will show up here.</p></div></c:when><c:otherwise>
<div class="table-wrap"><table><thead><tr><th>Student</th><th>Email</th><th>Account ID</th></tr></thead><tbody>
<c:forEach var="student" items="${students}"><tr><td class="table-primary"><c:out value="${student.fullName}" /></td><td><c:out value="${student.email}" /></td><td>#<c:out value="${student.id}" /></td></tr></c:forEach>
</tbody></table></div></c:otherwise></c:choose>
<%@ include file="includes/foot.jspf" %>
