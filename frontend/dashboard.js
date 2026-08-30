async function loadDashboard() {
    const list = document.getElementById("enrollments-list");
    const userId = localStorage.getItem("userId");
    if (!userId) {
        list.innerHTML = "<p>Please sign up or log in first.</p>";
        return;
    }
    try {
        const coursesResponse = await fetch("http://10.121.0.140:8080/api/courses/all");
        const allCourses = await coursesResponse.json();
        const courseMap = {};
        allCourses.forEach(function(course) {
            courseMap[course.id] = course;
        });
        const response = await fetch("http://10.121.0.140:8080/api/enrollments/user/" + userId);
        const enrollments = await response.json();
        if (enrollments.length === 0) {
            list.innerHTML = "<p>You haven't enrolled in any courses yet. Go to the Courses page to get started!</p>";
            return;
        }
        list.innerHTML = "";
        const circumference = 175.9;
        enrollments.forEach(function(enrollment) {
            const progress = enrollment.progressPercent;
            const offset = circumference - (circumference * progress / 100);
            const statusClass = enrollment.status === "COMPLETED" ? "completed" : "enrolled";
            const course = courseMap[enrollment.courseId];
            const courseTitle = course ? course.title : "Course #" + enrollment.courseId;

            const certificateButton = enrollment.status === "COMPLETED"
                ? "<button onclick=\"getCertificate(" + enrollment.id + ")\">Get Certificate</button>"
                : "";

            const card = document.createElement("div");
            card.className = "course-card";
            card.innerHTML =
                "<div class='enrollment-head'>" +
                    "<div>" +
                        "<h3>" + courseTitle + "</h3>" +
                        "<span class='status-pill " + statusClass + "'>" + enrollment.status + "</span>" +
                    "</div>" +
                    "<div class='mini-ring-wrap'>" +
                        "<svg viewBox='0 0 70 70' class='mini-ring'>" +
                            "<circle cx='35' cy='35' r='28' class='mini-ring-track'></circle>" +
                            "<circle cx='35' cy='35' r='28' class='mini-ring-fill' style='stroke-dasharray:" + circumference + "; stroke-dashoffset:" + offset + ";'></circle>" +
                        "</svg>" +
                        "<span class='mini-ring-percent'>" + progress + "%</span>" +
                    "</div>" +
                "</div>" +
                "<input type='range' min='0' max='100' value='" + progress + "' " +
                "id='slider-" + enrollment.id + "' " +
                "oninput=\"document.getElementById('label-" + enrollment.id + "').textContent = this.value + '%'\">" +
                "<p id='label-" + enrollment.id + "'>" + progress + "%</p>" +
                "<button onclick=\"saveProgress(" + enrollment.id + ")\">Save Progress</button> " +
                certificateButton;
            list.appendChild(card);
        });
    } catch (error) {
        list.innerHTML = "<p>Could not load your dashboard. Is the backend running?</p>";
    }
}

async function saveProgress(enrollmentId) {
    const slider = document.getElementById("slider-" + enrollmentId);
    const newProgress = slider.value;
    try {
        const response = await fetch(
            "http://10.121.0.140:8080/api/enrollments/" + enrollmentId + "/progress?progressPercent=" + newProgress,
            { method: "PUT" }
        );
        if (response.ok) {
            alert("Progress updated!");
            loadDashboard();
        } else {
            alert("Could not update progress.");
        }
    } catch (error) {
        alert("Could not reach the server.");
    }
}

async function getCertificate(enrollmentId) {
    try {
        const response = await fetch("http://10.121.0.140:8080/api/certificates/generate/" + enrollmentId, {
            method: "POST"
        });
        if (response.ok) {
            window.location.href = "certificates.html";
        } else {
            alert("Could not generate certificate.");
        }
    } catch (error) {
        alert("Could not reach the server.");
    }
}

function logout() {
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    localStorage.removeItem("userEmail");
    window.location.href = "index.html";
}

loadDashboard();