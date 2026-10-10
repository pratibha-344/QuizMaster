<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="includes/head.jspf" %><%@ include file="includes/nav.jspf" %>
<section class="error-panel"><span class="error-code">500</span><p class="eyebrow">WE HIT A SNAG</p><h1>That didn't go as planned.</h1><p class="muted"><c:out value="${errorMessage}" default="Please try again in a moment." /></p><a class="button button-primary" href="<c:url value='/' />">Back to QuizMaster</a></section>
<%@ include file="includes/foot.jspf" %>
