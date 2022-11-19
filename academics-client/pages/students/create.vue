<template>
  <div>
    <h1>Create a new Student</h1>
    <form @submit.prevent="create" :disabled="!isFormValid">
      <b-form-group
        id="username"
        description="The username is required"
        label="Enter your username"
        label-for="username"
        :invalid-feedback="invalidUsernameFeedback"
        :state="isUsernameValid"
      >
        <b-input
          id="username"
          v-model.trim="username"
          :state="isUsernameValid"
          trim
        ></b-input>
      </b-form-group>
      <b-input
        v-model="password"
        :state="isPasswordValid"
        required
        placeholder="Enter your password"
      />
      <b-input
        v-model.trim="name"
        :state="isNameValid"
        required
        placeholder="Enter your name"
      />
      <b-input
        ref="email"
        v-model.trim="email"
        type="email"
        :state="isEmailValid"
        required
        pattern=".+@my.ipleiria.pt"
        placeholder="Enter your e-mail"
      />
      <b-select
        v-model="courseCode"
        :options="courses"
        :state="isCourseValid"
        required
        value-field="code"
        text-field="name"
      >
        <template v-slot:first>
          <option :value="null" disabled>-- Please select the Course --</option>
        </template>
      </b-select>
      <p class="text-danger" v-show="errorMsg">{{ errorMsg }}</p>
      <nuxt-link to="/students">Return</nuxt-link>
      <button type="reset" @click="reset">RESET</button>
      <button @click.prevent="create" :disabled="!isFormValid">CREATE</button>
    </form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      username: null,
      password: null,
      name: null,
      email: null,
      courseCode: null,
      courses: [],
      errorMsg: false
    };
  },
  created() {
    this.$axios.$get("/api/courses").then((courses) => {
      this.courses = courses;
    });
  },
  computed: {
    invalidUsernameFeedback() {
      if (!this.username) {
        return null;
      }
      let usernameLen = this.username.length;
      if (usernameLen < 3 || usernameLen > 15) {
        return "The username must be between [3, 15] characters.";
      }
      return "";
    },
    isUsernameValid() {
      if (this.invalidUsernameFeedback === null) {
        return null;
      }
      return this.invalidUsernameFeedback === "";
    },
    isPasswordValid() {
      if (!this.password) {
        return null;
      }
      let passwordLen = this.password.length;
      if (passwordLen < 3 || passwordLen > 255) {
        return false;
      }
      return true;
    },
    isNameValid() {
      if (!this.name) {
        return null;
      }
      let nameLen = this.name.length;
      if (nameLen < 3 || nameLen > 25) {
        return false;
      }
      return true;
    },
    isEmailValid() {
      if (!this.email) {
        return null;
      }
      // asks the component if it’s valid. We don’t need to use a regex for the e - mail.The input field
      // already does the job for us, because it is of type “email” and validates that the user writes an
      // e - mail that belongs to the domain of IPLeiria.
      return this.$refs.email.checkValidity();
    },
    isCourseValid() {
      if (!this.courseCode) {
        return null;
      }
      return this.courses.some((course) => this.courseCode === course.code);
    },
    isFormValid() {
      if (!this.isUsernameValid) {
        return false;
      }
      if (!this.isPasswordValid) {
        return false;
      }
      if (!this.isNameValid) {
        return false;
      }
      if (!this.isEmailValid) {
        return false;
      }
      if (!this.isCourseValid) {
        return false;
      }
      return true;
    },
  },
  methods: {
    reset() {
      this.errorMsg = false;
    },
    create() {
      this.$axios
        .$post("/api/students", {
          username: this.username,
          password: this.password,
          name: this.name,
          email: this.email,
          courseCode: this.courseCode,
        })
        .then(() => {
          this.$router.push("/students");
        })
        .catch((error) => {
          this.errorMsg = error.response.data;
        });
    },
  },
};
</script>
