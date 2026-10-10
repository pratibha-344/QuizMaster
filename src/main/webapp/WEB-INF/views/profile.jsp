<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="page-heading"><p class="eyebrow">YOUR ACCOUNT</p><h1>Profile settings</h1><p class="muted">Keep your student details up to date.</p></section>
<c:if test="${not empty profileMessage}"><div class="notice notice-success"><c:out value="${profileMessage}" /></div></c:if>
<section class="settings-card">
  <div class="profile-avatar"><c:out value="${currentUser.fullName.substring(0,1).toUpperCase()}" /></div>
  <div><span class="tag">STUDENT ACCOUNT</span><h2><c:out value="${currentUser.fullName}" /></h2><p class="muted">Member of QuizMaster</p></div>
  <form method="post" action="<c:url value='/student/profile' />" class="form-stack settings-form">
    <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}' />">
    <label>Full name<input name="fullName" type="text" required minlength="2" maxlength="120" value="<c:out value='${currentUser.fullName}' />"></label>
    <label>Email address<input name="email" type="email" required maxlength="190" value="<c:out value='${currentUser.email}' />"></label>
    <button class="button button-primary" type="submit">Save changes</button>
  </form>
</section>
<%@ include file="includes/foot.jspf" %>
