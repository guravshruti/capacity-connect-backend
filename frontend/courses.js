async function loadCourses() {
    const coursesList = document.getElementById("courses-list");
    try {
        const response = await fetch("http://10.121.0.140:8080/api/courses/all");
        const courses = await response.json();
        if (courses.length === 0) {
            coursesList.innerHTML = "<p>No courses available yet.</p>";
            return;
        }
        coursesList.innerHTML = "";
        courses.forEach(function(course) {
            renderCourseCard(course, coursesList);
        });
    } catch (error) {
        coursesList.innerHTML = "<p>Could not load courses. Is the backend running?</p>";
    }
}

function renderCourseCard(course, container) {
    const card = document.createElement("div");
    card.className = "course-card";
    card.innerHTML =
        "<h3>" + course.title + "</h3>" +
        "<p>" + course.description + "</p>" +
        "<button onclick=\"enroll(" + course.id + ")\">Enroll →</button> " +
        "<button onclick=\"window.location.href='quiz.html?courseId=" + course.id + "'\">Take Quiz</button>" +
        "<div id='feedback-section-" + course.id + "' style='margin-top:18px; border-top:1px solid var(--line); padding-top:14px;'>" +
            "<span class='field-label'>Feedback</span>" +
            "<div id='feedback-list-" + course.id + "'><p>Loading feedback...</p></div>" +
            "<select id='feedback-rating-" + course.id + "'>" +
                "<option value='5'>5 - Excellent</option>" +
                "<option value='4'>4 - Good</option>" +
                "<option value='3'>3 - Okay</option>" +
                "<option value='2'>2 - Poor</option>" +
                "<option value='1'>1 - Very Poor</option>" +
            "</select>" +
            "<textarea id='feedback-comment-" + course.id + "' placeholder='Share your thoughts on this course...'></textarea>" +
            "<button onclick=\"submitFeedback(" + course.id + ")\">Submit Feedback</button>" +
        "</div>";
    container.appendChild(card);
    loadFeedback(course.id);
}

async function loadFeedback(courseId) {
    const list = document.getElementById("feedback-list-" + courseId);
    if (!list) return;

    try {
        const response = await fetch("http://10.121.0.140:8080/api/feedback/course/" + courseId);
        const feedbackItems = await response.json();

        if (feedbackItems.length === 0) {
            list.innerHTML = "<p>No feedback yet. Be the first to share yours.</p>";
            return;
        }

        list.innerHTML = "";
        feedbackItems.forEach(function(item) {
            const entry = document.createElement("p");
            entry.innerHTML = "★ " + item.rating + " — " + item.comment;
            list.appendChild(entry);
        });
    } catch (error) {
        list.innerHTML = "<p>Could not load feedback.</p>";
    }
}

async function submitFeedback(courseId) {
    const userId = localStorage.getItem("userId");
    if (!userId) {
        alert("Please sign up or log in first.");
        return;
    }

    const rating = document.getElementById("feedback-rating-" + courseId).value;
    const comment = document.getElementById("feedback-comment-" + courseId).value;

    if (!comment) {
        alert("Please write a comment before submitting.");
        return;
    }

    try {
        const response = await fetch("http://10.121.0.140:8080/api/feedback/add", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                userId: userId,
                courseId: courseId,
                rating: parseInt(rating),
                comment: comment
            })
        });

        if (response.ok) {
            document.getElementById("feedback-comment-" + courseId).value = "";
            loadFeedback(courseId);
        } else {
            alert("Could not submit feedback.");
        }
    } catch (error) {
        alert("Could not reach the server.");
    }
}

async function enroll(courseId) {
    const userId = localStorage.getItem("userId");
    if (!userId) {
        alert("Please sign up or log in first.");
        return;
    }
    try {
        const response = await fetch(
            "http://10.121.0.140:8080/api/enrollments/enroll?userId=" + userId + "&courseId=" + courseId,
            { method: "POST" }
        );
        if (response.ok) {
            alert("Enrolled successfully!");
        } else {
            alert("Could not enroll. You may already be enrolled in this course.");
        }
    } catch (error) {
        alert("Could not reach the server.");
    }
}

function renderCourses(courses) {
    const coursesList = document.getElementById("courses-list");
    if (courses.length === 0) {
        coursesList.innerHTML = "<p>No courses found.</p>";
        return;
    }
    coursesList.innerHTML = "";
    courses.forEach(function(course) {
        renderCourseCard(course, coursesList);
    });
}

async function loadAverageRating(courseId) {
    const el = document.getElementById("avg-" + courseId);
    if (!el) return;
    try {
        const response = await fetch("http://10.121.0.140:8080/api/ratings/course/" + courseId + "/average");
        const avg = await response.json();
        el.textContent = avg ? "★ " + avg.toFixed(1) + " average rating" : "No ratings yet";
    } catch (error) {
        el.textContent = "";
    }
}

async function submitRating(courseId, stars) {
    const userId = localStorage.getItem("userId");
    if (!userId) {
        alert("Please sign up or log in first.");
        return;
    }
    try {
        const response = await fetch(
            "http://10.121.0.140:8080/api/ratings/add?userId=" + userId + "&courseId=" + courseId + "&stars=" + stars,
            { method: "POST" }
        );
        if (response.ok) {
            alert("Thanks for rating!");
            loadAverageRating(courseId);
        } else {
            alert("Could not submit rating.");
        }
    } catch (error) {
        alert("Could not reach the server.");
    }
}

async function searchCourses() {
    const keyword = document.getElementById("search-box").value.trim();
    const coursesList = document.getElementById("courses-list");
    if (keyword === "") {
        loadCourses();
        return;
    }
    try {
        const response = await fetch("http://10.121.0.140:8080/api/courses/search?keyword=" + encodeURIComponent(keyword));
        const courses = await response.json();
        renderCourses(courses);
    } catch (error) {
        coursesList.innerHTML = "<p>Could not search courses. Is the backend running?</p>";
    }
}

loadCourses();

function logout() {
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    localStorage.removeItem("userEmail");
    window.location.href = "index.html";
}