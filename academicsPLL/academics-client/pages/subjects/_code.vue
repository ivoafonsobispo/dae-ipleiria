<template>
  <b-container>
    <h4>Subjects Details:</h4>
    <p>Code: {{ subject.code }}</p>
    <p>Course Code: {{ subject.courseCode }}</p>
    <p>Course Name: {{ subject.courseName }}</p>
    <p>Course Year: {{ subject.courseYear }}</p>
    <p>Name: {{ subject.name }}</p>
    <p>Schoolar Year: {{ subject.schoolarYear }}</p>
    <h4>Students enrolled:</h4>
    <b-table
      v-if="students.length"
      striped
      over
      :items="students"
      :fields="studentFields"
    />
    <p v-else>No students enrolled.</p>
    <nuxt-link to="/subjects">Back</nuxt-link>
  </b-container>
</template>
<script>
export default {
  data() {
    return {
      subject: {},
      students: [],
      studentFields: ["code", "name", "email", "courseName"],
    };
  },
  computed: {
    code() {
      return this.$route.params.code;
    },
  },
  created() {
    this.$axios
      .$get(`/api/subjects/${this.code}`)
      .then((subject) => (this.subject = subject || {}))
      .then(() => this.$axios.$get(`/api/subjects/${this.code}/students`))
      .then((students) => (this.students = students));
  },
};
</script>
