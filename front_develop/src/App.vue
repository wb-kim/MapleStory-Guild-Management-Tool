<template>
  <div class="v-application" :class="{ 'nav-open': $sidebar.showSidebar }">
    <notifications></notifications>
    <router-view></router-view>
  </div>
</template>

<script>
export default {};
</script>

<style lang="scss">
.vue-notifyjs.notifications {
  .alert {
    z-index: 10000;
  }
  .list-move {
    transition: transform 0.3s, opacity 0.4s;
  }
  .list-item {
    display: inline-block;
    margin-right: 10px;
  }
  .list-enter-active {
    transition: transform 0.2s ease-in, opacity 0.4s ease-in;
  }
  .list-leave-active {
    transition: transform 1s ease-out, opacity 0.4s ease-out;
  }

  .list-enter {
    opacity: 0;
    transform: scale(1.1);
  }
  .list-leave-to {
    opacity: 0;
    transform: scale(1.2, 0.7);
  }
}
.v-application {
  font-family: 'NanumGothic', sans-serif !important;
}
@font-face {
  font-family:'NanumGothic';
  src: url('assets/fonts/NanumGothic.ttf') format('truetype');
  font-weight: 400;
}
@font-face {
  font-family:'NanumGothic';
  src: url('assets/fonts/NanumGothicLight.ttf') format('truetype');
  font-weight: 300;
}
@font-face {
  font-family:'NanumGothic';
  src: url('assets/fonts/NanumGothicBold.ttf') format('truetype');
  font-weight: 700;
}

button {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  
  background: var(--button-bg-color);
  color: var(--button-color);
  
  margin: 0;
  padding: 3px 6px;
  
  font-family: 'NanumGothic';
  font-weight: 400;
  text-align: center;
  text-decoration: none;
  
  border: none;
  border-radius: 4px;
  
  display: inline-block;
  width: auto;
  
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  
  cursor: pointer;
  
  transition: 0.5s;
}

button.success {
  --button-bg-color: #28a745;
  --button-hover-bg-color: #218838;
}

button.error {
  --button-bg-color: #dc3545;
  --button-hover-bg-color: #c82333;
}

button.warning {
  --button-color: #212529;
  --button-bg-color: #ffc107;
  --button-hover-bg-color: #e0a800;
}

button:active,
button:hover,
button:focus {
  background: var(--button-hover-bg-color);
  outline: 0;
}
button:disabled {
  opacity: 0.5;
}
</style>
