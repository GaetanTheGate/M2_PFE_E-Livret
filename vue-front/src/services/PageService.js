class PageService {
    router

    constructor(router) {
        this.router = router;
    }

    gotoLoginPage() {
        this.router.push({ path: "/Login" });
    }

    gotoProfilePage() {
        this.router.push({ path: "/Profile" });
    }
}

export default PageService