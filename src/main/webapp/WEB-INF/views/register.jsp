<%@ include file="includes/head.jspf" %>
<%@ include file="includes/nav.jspf" %>
<section class="auth-layout register-layout">
  <div class="auth-story">
    <p class="eyebrow">MAKE ROOM TO LEARN</p>
    <h1>Every expert started with a <span>question.</span></h1>
    <p class="muted large-copy">Create your student account and turn a few minutes of practice into real momentum.</p>
    <div class="feature-points"><span>Timed quizzes</span><span>Instant results</span><span>Your own progress history</span></div>
  </div>
  <div class="auth-card">
    <p class="eyebrow">GET STARTED</p><h2>Create your account</h2>
    <p class="muted">Join as a student in less than a minute.</p>
    <form method="post" action="<c:url value='/register' />" class="form-stack">
      <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}' />">
      <label>Full name<input type="text" name="fullName" autocomplete="name" required minlength="2" maxlength="120" placeholder="Your name"></label>
      <label>Email address<input type="email" name="email" autocomplete="email" required maxlength="190" placeholder="you@example.com"></label>
      <label>Password<input type="password" name="password" autocomplete="new-password" required minlength="8" maxlength="128" placeholder="At least 8 characters"></label>
      <label>Confirm password<input type="password" name="confirmPassword" autocomplete="new-password" required></label>
      <button class="button button-primary button-wide" type="submit">Create student account <span aria-hidden="true">→</span></button>
    </form>
    <p class="auth-switch">Already have an account? <a href="<c:url value='/login' />">Log in</a></p>
  </div>
</section>
<%@ include file="includes/foot.jspf" %>
