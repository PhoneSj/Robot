/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zige.zige.httplibray;

import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * Delivers responses and errors.
 */
public class ExecutorDelivery implements Delivery {
    /** Used for posting responses, typically to the main thread. */
    private final Executor mResponsePoster;

    /**
     * Creates a new response delivery interface.
     * @param handler {@link Handler} to post responses on
     */
    public ExecutorDelivery(final Handler handler) {
        // Make an Executor that just wraps the handler.
        mResponsePoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    /**
     * Creates a new response delivery interface, mockable version
     * for testing.
     * @param executor For running delivery tasks
     */
    public ExecutorDelivery(Executor executor) {
        mResponsePoster = executor;
    }

	@Override
	public void postFinish(final Request<?> request) {
		request.addMarker("post-finish");
		mResponsePoster.execute(new Runnable() {
			@Override
			public void run() {
				request.deliverFinish();
			}
		});
	}

	@Override
    public void postResponse(Request<?> request, Response<?> response) {
        postResponse(request, response, null);
    }

    @Override
    public void postResponse(Request<?> request, Response<?> response, Runnable runnable) {
        request.markDelivered();
        request.addMarker("post-response");
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, runnable));
    }

    @Override
    public void postError(Request<?> request, NetroidError error) {
        request.addMarker("post-error");
        Response<?> response = Response.error(error);
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, response, null));
    }

    @Override
    public void postCancel(final Request<?> request) {
        request.addMarker("post-cancel");
        mResponsePoster.execute(new Runnable() {
			@Override
			public void run() {
				request.deliverCancel();
			}
		});
    }

	@Override
	public void postPreExecute(final Request<?> request) {
		request.addMarker("post-preexecute");
		mResponsePoster.execute(new Runnable() {
			@Override
			public void run() {
				request.deliverPreExecute();
			}
		});
	}

	@Override
	public void postUsedCache(final Request<?> request) {
		request.addMarker("post-preexecute");
		mResponsePoster.execute(new Runnable() {
			@Override
			public void run() {
				request.deliverUsedCache();
			}
		});
	}

	@Override
	public void postNetworking(final Request<?> request) {
		request.addMarker("post-networking");
		mResponsePoster.execute(new Runnable() {
			@Override
			public void run() {
				request.deliverNetworking();
			}
		});
	}

	@Override
	public void postRetry(final Request<?> request) {
		request.addMarker("post-preexecute");
		mResponsePoster.execute(new Runnable() {
			@Override
			public void run() {
				request.deliverRetry();
			}
		});
	}

	@Override
	public void postDownloadProgress(final Request<?> request, final long fileSize, final long downloadedSize) {
		request.addMarker("post-downloadprogress");
		mResponsePoster.execute(new Runnable() {
			@Override
			public void run() {
				request.deliverDownloadProgress(fileSize, downloadedSize);
			}
		});
	}

	/**
     * A Runnable used for delivering network responses to a listener on the
     * main thread.
     */
    @SuppressWarnings("rawtypes")
    private class ResponseDeliveryRunnable implements Runnable {
        private final Request mRequest;
        private final Response mResponse;
        private final Runnable mRunnable;

        public ResponseDeliveryRunnable(Request request, Response response, Runnable runnable) {
            mRequest = request;
            mResponse = response;
            mRunnable = runnable;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            // If this request has canceled, finish it and don't deliver.
            if (mRequest.isCanceled()) {
                mRequest.finish("canceled-at-delivery");
				mRequest.deliverFinish();
                return;
            }

            // Deliver a normal response or error, depending.
            if (mResponse.isSuccess()) {
                mRequest.deliverSuccess(mResponse.result);
            } else {
                mRequest.deliverError(mResponse.error);
            }

            // If this is an intermediate response, add a marker, otherwise we're done
            // and the request can be finished.
            if (mResponse.intermediate) {
                mRequest.addMarker("intermediate-response");
            } else {
                mRequest.finish("done");
            }

            // If we have been provided a post-delivery runnable, run it.
            if (mRunnable != null) {
                mRunnable.run();
            }

			mRequest.deliverFinish();
       }
    }
}
