<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="page-heading"><p class="eyebrow">QUIZ BUILDER</p><h1><c:choose><c:when test="${quiz.id gt 0}">Edit quiz</c:when><c:otherwise>Create a quiz</c:otherwise></c:choose></h1><p class="muted">Set the basics first. You can add or edit questions next.</p></section>
<c:if test="${quiz.id gt 0}"><div class="inline-callout">Questions for this quiz can be managed from the <a href="<c:url value='/admin/questions?quizId=${quiz.id}' />">question editor</a>.</div></c:if>
<section class="form-panel">
<form action="<c:url value='/admin/quizzes/save' />" method="post" class="form-stack">
  <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}' />"><input type="hidden" name="id" value="<c:out value='${quiz.id}' default='0' />">
  <label>Quiz title<input type="text" name="title" required minlength="3" maxlength="160" value="<c:out value='${quiz.title}' />" placeholder="For example, Java Fundamentals"></label>
  <label>Description<textarea name="description" rows="4" maxlength="4000" placeholder="What will students learn?"><c:out value="${quiz.description}" /></textarea></label>
  <label>Time limit in minutes<input type="number" name="timeLimitMinutes" required min="1" max="240" value="<c:choose><c:when test='${quiz.timeLimitMinutes gt 0}'><c:out value='${quiz.timeLimitMinutes}' /></c:when><c:otherwise>15</c:otherwise></c:choose>"></label>
  <label class="check-row"><input type="checkbox" name="published" value="true" <c:if test="${quiz eq null or quiz.published}">checked</c:if>> Publish this quiz so students can take it</label>
  <div class="button-row"><a class="button button-outline" href="<c:url value='/admin/quizzes' />">Cancel</a><button class="button button-primary" type="submit">Save quiz</button></div>
</form>
</section>
<%@ include file="includes/foot.jspf" %>
