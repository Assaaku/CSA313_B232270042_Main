const fs = require("fs");

const DOUBLE_ERROR = process.env.DOUBLE_ERROR;

if (!DOUBLE_ERROR) {
    console.error("Please set DOUBLE_ERROR first.");
    console.error("Example:");
    console.error("DOUBLE_ERROR=ERROR_INACTIVE_STUDENT node generate-collection.js");
    process.exit(1);
}

function request(name, method, url, body = null, tests = []) {
    const req = {
        name: name,
        request: {
            method: method,
            header: [
                {
                    key: "Content-Type",
                    value: "application/json",
                    type: "text"
                }
            ],
            url: `{{baseUrl}}${url}`
        }
    };

    if (body !== null) {
        req.request.body = {
            mode: "raw",
            raw: JSON.stringify(body, null, 2),
            options: {
                raw: {
                    language: "json"
                }
            }
        };
    }

    if (tests.length > 0) {
        req.event = [
            {
                listen: "test",
                script: {
                    type: "text/javascript",
                    exec: tests
                }
            }
        ];
    }

    return req;
}

function oracle(status, result) {
    return [
        `pm.test("Status ${status}", function () {`,
        `    pm.response.to.have.status(${status});`,
        `});`,
        ``,
        `pm.test("Result is ${result}", function () {`,
        `    pm.expect(pm.response.json().result).to.eql("${result}");`,
        `});`
    ];
}

const collection = {
    info: {
        name: "Lab05 API System Test",
        description: "F.CSA313 Lab 05 - Postman and Newman",
        schema: "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
    },

    variable: [
        {
            key: "baseUrl",
            value: "http://localhost:3000"
        }
    ],

    item: [

        // 1. HAPPY PATH
        {
            name: "01 - Happy Path",
            item: [
                request(
                    "Setup active student",
                    "PUT",
                    "/students/L5_HAPPY",
                    {
                        status: "active",
                        coursesTaken: ["CS201"]
                    }
                ),

                request(
                    "Setup course requiring CS201",
                    "PUT",
                    "/courses/L5_HAPPY_COURSE",
                    {
                        prerequisites: ["CS201"]
                    }
                ),

                request(
                    "Register successfully",
                    "POST",
                    "/registrations",
                    {
                        studentID: "L5_HAPPY",
                        courseID: "L5_HAPPY_COURSE"
                    },
                    oracle(201, "OK")
                )
            ]
        },

        // 2. STUDENT DOES NOT EXIST
        {
            name: "02 - Student Does Not Exist",
            item: [
                request(
                    "Setup course",
                    "PUT",
                    "/courses/L5_NO_STUDENT_COURSE",
                    {
                        prerequisites: []
                    }
                ),

                request(
                    "Missing student registration",
                    "POST",
                    "/registrations",
                    {
                        studentID: "L5_DOES_NOT_EXIST",
                        courseID: "L5_NO_STUDENT_COURSE"
                    },
                    oracle(200, "ERROR_NO_STUDENT")
                )
            ]
        },

        // 3. INACTIVE STUDENT
        {
            name: "03 - Inactive Student",
            item: [
                request(
                    "Setup inactive student",
                    "PUT",
                    "/students/L5_INACTIVE",
                    {
                        status: "inactive",
                        coursesTaken: []
                    }
                ),

                request(
                    "Setup course",
                    "PUT",
                    "/courses/L5_INACTIVE_COURSE",
                    {
                        prerequisites: []
                    }
                ),

                request(
                    "Inactive student registration",
                    "POST",
                    "/registrations",
                    {
                        studentID: "L5_INACTIVE",
                        courseID: "L5_INACTIVE_COURSE"
                    },
                    oracle(200, "ERROR_INACTIVE_STUDENT")
                )
            ]
        },

        // 4. COURSE DOES NOT EXIST
        {
            name: "04 - Course Does Not Exist",
            item: [
                request(
                    "Setup active student",
                    "PUT",
                    "/students/L5_NO_COURSE",
                    {
                        status: "active",
                        coursesTaken: []
                    }
                ),

                request(
                    "Missing course registration",
                    "POST",
                    "/registrations",
                    {
                        studentID: "L5_NO_COURSE",
                        courseID: "L5_COURSE_DOES_NOT_EXIST"
                    },
                    oracle(200, "ERROR_NO_COURSE")
                )
            ]
        },

        // 5. PREREQUISITE MISSING
        {
            name: "05 - Missing Prerequisite",
            item: [
                request(
                    "Setup student without CS201",
                    "PUT",
                    "/students/L5_NO_PREREQ",
                    {
                        status: "active",
                        coursesTaken: []
                    }
                ),

                request(
                    "Setup course requiring CS201",
                    "PUT",
                    "/courses/L5_NEEDS_CS201",
                    {
                        prerequisites: ["CS201"]
                    }
                ),

                request(
                    "Register without prerequisite",
                    "POST",
                    "/registrations",
                    {
                        studentID: "L5_NO_PREREQ",
                        courseID: "L5_NEEDS_CS201"
                    },
                    oracle(200, "ERROR_PREREQUISITES")
                )
            ]
        },

        // 6. NO PREREQUISITES
        {
            name: "06 - No Prerequisites Boundary",
            item: [
                request(
                    "Setup student with no courses",
                    "PUT",
                    "/students/L5_EMPTY",
                    {
                        status: "active",
                        coursesTaken: []
                    }
                ),

                request(
                    "Setup course with no prerequisites",
                    "PUT",
                    "/courses/L5_NO_PREREQUISITES",
                    {
                        prerequisites: []
                    }
                ),

                request(
                    "Register for no-prerequisite course",
                    "POST",
                    "/registrations",
                    {
                        studentID: "L5_EMPTY",
                        courseID: "L5_NO_PREREQUISITES"
                    },
                    oracle(201, "OK")
                )
            ]
        },

        // 7. MISSING REQUIRED FIELD
        {
            name: "07 - Missing Required Field",
            item: [
                request(
                    "Setup student",
                    "PUT",
                    "/students/L5_BAD_REQUEST",
                    {
                        status: "active",
                        coursesTaken: []
                    }
                ),

                request(
                    "Register without courseID",
                    "POST",
                    "/registrations",
                    {
                        studentID: "L5_BAD_REQUEST"
                    },
                    oracle(400, "ERROR_BAD_REQUEST")
                )
            ]
        },

        // 8. DOUBLE ERROR
        {
            name: "08 - Double Error",
            item: [
                request(
                    "Setup inactive student",
                    "PUT",
                    "/students/L5_DOUBLE",
                    {
                        status: "inactive",
                        coursesTaken: []
                    }
                ),

                request(
                    "Inactive student and missing course",
                    "POST",
                    "/registrations",
                    {
                        studentID: "L5_DOUBLE",
                        courseID: "L5_MISSING_COURSE"
                    },
                    oracle(200, DOUBLE_ERROR)
                )
            ]
        }
    ]
};

fs.writeFileSync(
    "lab05-collection.json",
    JSON.stringify(collection, null, 2)
);

console.log("");
console.log("Created: lab05-collection.json");
console.log("Scenarios: 8");
console.log("Expected registration assertions: 16");
console.log("Double-error result:", DOUBLE_ERROR);
console.log("");
