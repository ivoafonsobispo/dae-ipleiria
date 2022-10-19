<template>
  <b-container>
    <h4>Student Details:</h4>
    <p>Username: {{ student.username }}</p>
    <p>Name: {{ student.name }}</p>
    <p>Email: {{ student.email }}</p>
    <p>Course: {{ student.courseName }}</p>
    <h4>Subjects enrolled:</h4>
    <b-table
      v-if="subjects.length"
      striped
      over
      :items="subjects"
      :fields="subjectFields"
    />
    <p v-else>No subjects enrolled.</p>
    <nuxt-link to="/students">Back</nuxt-link>
  </b-container>
</template>
<script>
export default {
  data() {
    return {
      student: {},
      subjects: [],
      subjectFields: [
        "code",
        "name",
        "courseCode",
        "courseYear",
        "scholarYear",
      ],
    };
  },
  computed: {
    username() {
      return this.$route.params.username;
    },
  },
  created() {
    this.$axios
      .$get(`/api/students/${this.username}`)
      .then((student) => (this.student = student || {}))
      .then(() => this.$axios.$get(`/api/students/${this.username}/subjects`))
      .then((subjects) => (this.subjects = subjects));
  },
};
</script>
