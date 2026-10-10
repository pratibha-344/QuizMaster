<%@ include file="includes/head.jspf" %>
<%@ include file="includes/nav.jspf" %>
<section class="auth-layout">
  <div class="auth-story">
    <p class="eyebrow">YOUR NEXT BIG IDEA STARTS HERE</p>
    <h1>Curiosity is a skill. <span>Keep practicing.</span></h1>
    <p class="muted large-copy">A focused space to test what you know, learn what you don't, and see your progress take shape.</p>
    <div class="story-stat"><strong>12</strong><span>ready-to-try sample questions</span></div>
  </div>
  <div class="auth-card">
    <p class="eyebrow">WELCOME BACK</p><h2>Log in to QuizMaster</h2>
    <p class="muted">Pick up where your learning left off.</p>
    <form method="post" action="<c:url value='/login' />" class="form-stack">
      <input type="hidden" name="_csrf" value="<c:out value='${csrfToken}' />">
      <label>Email address<input type="email" name="email" autocomplete="username" required maxlength="190" placeholder="you@example.com"></label>
      <label>Password<input type="password" name="password" autocomplete="current-password" required></label>
      <button class="button button-primary button-wide" type="submit">Log in <span aria-hidden="true">→</span></button>
    </form>
    <p class="auth-switch">New to QuizMaster? <a href="<c:url value='/register' />">Create your student account</a></p>
    <div class="demo-credentials"><strong>Demo access</strong><span>Student: student@quizmaster.com · student123</span><span>Admin: admin@quizmaster.com · admin123</span></div>
  </div>
</section>
<%@ include file="includes/foot.jspf" %>
