document$.subscribe(() => {
  const repoLink = document.querySelector("a.md-source");

  if (repoLink) {
    repoLink.target = "_blank";
    repoLink.rel = "noopener noreferrer";
  }
});
