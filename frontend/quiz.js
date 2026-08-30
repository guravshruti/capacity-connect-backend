const urlParams = new URLSearchParams(window.location.search);
const courseId = urlParams.get("courseId");
let quizQuestions = [];

async function loadQuiz() {
    const container = document.getElementById("quiz-container");

    if (!courseId) {
        container.innerHTML = "<p>No course selected. Go back to Courses and click Take Quiz.</p>";
        return;
    }

    try {
        const courseResponse = await fetch("http://10.121.0.140:8080/api/courses/all");
        const allCourses = await courseResponse.json();
        const course = allCourses.find(function(c) { return c.id == courseId; });

        if (course) {
            document.getElementById("quiz-title").textContent = course.title + " - Quiz";
        }

        let response = await fetch("http://10.121.0.140:8080/api/quiz/" + courseId);
        let questions = await response.json();

        if (questions.length === 0) {
            container.innerHTML = "<p>Generating quiz questions...</p>";
            const genResponse = await fetch("http://10.121.0.140:8080/api/quiz/generate", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    courseId: courseId,
                    title: course ? course.title : "",
                    description: course ? course.description : ""
                })
            });
            questions = await genResponse.json();
        }

        quizQuestions = questions;
        renderQuiz(questions);
    } catch (error) {
        container.innerHTML = "<p>Could not load quiz. Is the backend running?</p>";
    }
}

function renderQuiz(questions) {
    const container = document.getElementById("quiz-container");

    if (questions.length === 0) {
        container.innerHTML = "<p>No quiz questions available for this course yet.</p>";
        return;
    }

    container.innerHTML = "";
    questions.forEach(function(q, index) {
        const card = document.createElement("div");
        card.className = "course-card";
        card.innerHTML =
            "<h3>" + (index + 1) + ". " + q.questionText + "</h3>" +
            "<label><input type='radio' name='q-" + q.id + "' value='A'> " + q.optionA + "</label><br>" +
            "<label><input type='radio' name='q-" + q.id + "' value='B'> " + q.optionB + "</label><br>" +
            "<label><input type='radio' name='q-" + q.id + "' value='C'> " + q.optionC + "</label><br>" +
            "<label><input type='radio' name='q-" + q.id + "' value='D'> " + q.optionD + "</label>";
        container.appendChild(card);
    });

    const submitBtn = document.createElement("button");
    submitBtn.textContent = "Submit Quiz";
    submitBtn.onclick = submitQuiz;
    container.appendChild(submitBtn);
}

async function submitQuiz() {
    const resultBox = document.getElementById("quiz-result");
    const submissions = [];

    for (const q of quizQuestions) {
        const selected = document.querySelector("input[name='q-" + q.id + "']:checked");
        if (!selected) {
            resultBox.innerHTML = "<p style='color:red;'>Please answer all questions before submitting.</p>";
            return;
        }
        submissions.push({ questionId: q.id, selectedAnswer: selected.value });
    }

    try {
        const response = await fetch("http://10.121.0.140:8080/api/quiz/submit", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(submissions)
        });

        if (response.ok) {
            const result = await response.json();
            resultBox.innerHTML =
                "<div class='course-card'>" +
                "<h3>Quiz Result</h3>" +
                "<p>Score: " + result.score + "%</p>" +
                "<p>" + result.correctCount + " out of " + result.totalQuestions + " correct</p>" +
                "</div>";
        } else {
            resultBox.innerHTML = "<p style='color:red;'>Could not submit quiz.</p>";
        }
    } catch (error) {
        resultBox.innerHTML = "<p style='color:red;'>Could not reach the server.</p>";
    }
}

function logout() {
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    localStorage.removeItem("userEmail");
    window.location.href = "index.html";
}

loadQuiz();