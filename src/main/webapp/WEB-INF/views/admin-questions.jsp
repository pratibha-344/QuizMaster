<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="page-heading page-heading-row"><div><p class="eyebrow">QUESTION EDITOR</p><h1><c:out value="${quiz.title}" /></h1><p class="muted">Add, edit, or remove this quiz's multiple-choice questions.</p></div><a class="button button-outline" href="<c:url value='/admin/quizzes' />">← Back to quizzes</a></section>
<section class="form-panel question-editor">
  <div class="section-heading"><div><p class="eyebrow">NEW QUESTION</p><h2>Add a question</h2></div></div>
  <form action="<c:url value='/admin/questions/save' />" method="post" class="form-stack">
    <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}' />"><input type="hidden" name="quizId" value="<c:out value='${quiz.id}' />"><input type="hidden" name="questionId" value="0">
    <label>Question<textarea name="questionText" rows="2" required maxlength="4000" placeholder="Write the question"></textarea></label>
    <div class="options-grid admin-options">
      <label>Option A<input name="optionA" required maxlength="500"></label><label>Option B<input name="optionB" required maxlength="500"></label>
      <label>Option C<input name="optionC" required maxlength="500"></label><label>Option D<input name="optionD" required maxlength="500"></label>
    </div>
    <div class="form-row"><label>Correct option<select name="correctOption" required><option value="">Choose answer</option><option>A</option><option>B</option><option>C</option><option>D</option></select></label><button class="button button-primary" type="submit">Add question</button></div>
  </form>
</section>
<section class="content-section">
  <div class="section-heading"><div><p class="eyebrow">EXISTING QUESTIONS</p><h2><c:out value="${questions.size()}" /> total</h2></div></div>
  <c:choose><c:when test="${empty questions}"><div class="empty-state"><strong>No questions in this quiz</strong><p>Use the form above to add the first one.</p></div></c:when><c:otherwise>
    <c:forEach var="question" items="${questions}" varStatus="loop">
      <article class="edit-question-card">
        <div class="section-heading"><h3>Question <c:out value="${loop.count}" /></h3>
          <form action="<c:url value='/admin/questions/delete' />" method="post" data-confirm="Delete this question?">
            <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}' />"><input type="hidden" name="quizId" value="<c:out value='${quiz.id}' />"><input type="hidden" name="questionId" value="<c:out value='${question.id}' />"><button class="text-button danger-text" type="submit">Delete</button>
          </form>
        </div>
        <form action="<c:url value='/admin/questions/save' />" method="post" class="form-stack">
          <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}' />"><input type="hidden" name="quizId" value="<c:out value='${quiz.id}' />"><input type="hidden" name="questionId" value="<c:out value='${question.id}' />">
          <label>Question<textarea name="questionText" rows="2" required maxlength="4000"><c:out value="${question.text}" /></textarea></label>
          <div class="options-grid admin-options">
            <label>Option A<input name="optionA" required maxlength="500" value="<c:out value='${question.optionA}' />"></label><label>Option B<input name="optionB" required maxlength="500" value="<c:out value='${question.optionB}' />"></label>
            <label>Option C<input name="optionC" required maxlength="500" value="<c:out value='${question.optionC}' />"></label><label>Option D<input name="optionD" required maxlength="500" value="<c:out value='${question.optionD}' />"></label>
          </div>
          <div class="form-row"><label>Correct option<select name="correctOption" required><option value="A" <c:if test="${question.correctOption eq 'A'}">selected</c:if>>A</option><option value="B" <c:if test="${question.correctOption eq 'B'}">selected</c:if>>B</option><option value="C" <c:if test="${question.correctOption eq 'C'}">selected</c:if>>C</option><option value="D" <c:if test="${question.correctOption eq 'D'}">selected</c:if>>D</option></select></label><button class="button button-outline" type="submit">Save changes</button></div>
        </form>
      </article>
    </c:forEach>
  </c:otherwise></c:choose>
</section>
<%@ include file="includes/foot.jspf" %>
